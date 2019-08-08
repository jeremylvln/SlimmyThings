#!/bin/bash

ROOT="../src/main/resources/assets/slimeperipherals"
OUTPUT="$ROOT/recipes/generated"

colors=(white orange magenta lightblue yellow lime pink gray silver cyan purple blue brown green red black)
data_src=(15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0)
data_dest=(0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15)

len=${#colors[@]}

for (( i=1; i<$len; i++ )); do
    name="magnetic_card_${colors[$i]}"
	cat > $OUTPUT/$name.json <<EOF
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "slimeperipherals:magnetic_card",
      "data": 0
    },
    {
      "item": "minecraft:dye",
      "data": ${data_src[$i]}
    }
  ],
  "result": {
    "item": "slimeperipherals:magnetic_card",
    "data": ${data_dest[$i]},
    "count": 1
  }
}
EOF
    if [ ! $? -eq 0 ]; then
        exit 1
    fi
    echo "Generated recipe $name."
done

for (( i=1; i<$len; i++ )); do
    name="magnetic_card_${colors[$i]}_reset"
	cat > $OUTPUT/$name.json <<EOF
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "slimeperipherals:magnetic_card",
      "data": ${data_dest[$i]}
    },
    {
      "item": "minecraft:water_bucket",
      "data": 0
    }
  ],
  "result": {
    "item": "slimeperipherals:magnetic_card",
    "data": 0,
    "count": 1
  }
}
EOF
    if [ ! $? -eq 0 ]; then
        exit 1
    fi
    echo "Generated recipe $name."
done

for (( i=1; i<$len; i++ )); do
    name="rfid_card_${colors[$i]}"
	cat > $OUTPUT/$name.json <<EOF
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "slimeperipherals:rfid_card",
      "data": 0
    },
    {
      "item": "minecraft:dye",
      "data": ${data_src[$i]}
    }
  ],
  "result": {
    "item": "slimeperipherals:rfid_card",
    "data": ${data_dest[$i]},
    "count": 1
  }
}
EOF
    if [ ! $? -eq 0 ]; then
        exit 1
    fi
    echo "Generated recipe $name."
done

for (( i=1; i<$len; i++ )); do
    name="rfid_card_${colors[$i]}_reset"
	cat > $OUTPUT/$name.json <<EOF
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "slimeperipherals:rfid_card",
      "data": ${data_dest[$i]}

    },
    {
      "item": "minecraft:water_bucket",
      "data": 0
    }
  ],
  "result": {
    "item": "slimeperipherals:rfid_card",
    "data": 0,
    "count": 1
  }
}
EOF
    if [ ! $? -eq 0 ]; then
        exit 1
    fi
    echo "Generated recipe $name."
done