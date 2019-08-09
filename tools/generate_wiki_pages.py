#!/bin/env python3

import os
import shutil
from pathlib import Path
from typing import List

import click
import git
import toml
import tqdm


ROOT_PATH = "wiki"
GIT_REPOSITORY = "git@github.com:IamBlueSlime/SlimmyThings.wiki.git"
GIT_REPOSITORY_PATH = ROOT_PATH + os.sep + ".tmp_wiki.git"


def scan_for_files(path: str) -> List[str]:
    found = []
    for root, _, files in os.walk(path):
        for file in files:
            if file.endswith(".toml"):
                found.append(os.path.join(root, file))

    return found


def process_file(path: str):
    parsed_file = toml.load(path)
    final_file = ""

    final_file += "## Description\n\n"
    final_file += parsed_file["description"]
    final_file += "\n\n"

    if "methods" in parsed_file:
        final_file += "## Available methods\n\n"

        for method in parsed_file["methods"]:
            formatted_params = ", ".join(method["params"]) if "params" in method else ""
            formatted_returns = " -> " + ", ".join(method["returns"]) if "returns" in method else ""
            final_file += "* `{}({}){}`: {}\n".format(
                method["name"], formatted_params, formatted_returns, method["description"]
            )

        final_file += "\n\n"

    if "notes" in parsed_file:
        final_file += "## Notes\n\n"
        final_file += parsed_file["notes"]
        final_file += "\n\n"

    if "examples" in parsed_file:
        final_file += "## Examples\n\n"

        for example in parsed_file["examples"]:
            final_file += "### {}\n\n".format(example["name"])
            final_file += "```lua\n{}\n```\n".format(example["content"])
            final_file += "\n\n"

        final_file += "\n\n"

    final_file = final_file.rstrip() + "\n"

    with open(path.replace(".toml", ".md"), "w") as out:
        out.write(final_file)


def push_pages(files: List[str], repository: str):
    if not Path(GIT_REPOSITORY_PATH).exists():
        print("Cloning repository...")
        repo = git.Repo.clone_from(repository, GIT_REPOSITORY_PATH, branch="master")
    else:
        print("Updating existing repository...")
        repo = git.Repo(GIT_REPOSITORY_PATH)
        repo.remotes[0].pull()

    print("Copying files...")
    for file in files:
        markdown_file = file.replace(".toml", ".md")
        copy_path = os.path.join(GIT_REPOSITORY_PATH, os.path.basename(markdown_file))
        print("{} -> {}".format(markdown_file, copy_path))
        shutil.copyfile(markdown_file, copy_path)
        repo.index.add([os.path.basename(markdown_file)])

    print("Commiting...")
    repo.index.commit("Update")

    print("Pushing...")
    repo.remotes[0].push()


if __name__ == '__main__':
    print("Generating files...")
    files = scan_for_files(ROOT_PATH)
    with tqdm.tqdm(total=len(files)) as bar:
        for file in files:
            process_file(file)
            bar.update()

    if click.confirm("Do you want to update the GitHub repository?", default=False):
        push_pages(files, GIT_REPOSITORY)
