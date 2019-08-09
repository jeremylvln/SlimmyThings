#!/bin/bash

ROOT="../src/main/resources/assets/slimmythings"

for file in $ROOT/textures/items/*_card_*.png; do
	base=$(basename $file)
	name=${base%.*}
	cat > $ROOT/models/item/$name.json <<EOF
{
	"parent": "minecraft:item/generated",
	"textures": {
		"layer0": "slimmythings:items/$name"
	}
}
EOF
    if [ ! $? -eq 0 ]; then
        exit 1
    fi
    echo "Generated model $name."
done
