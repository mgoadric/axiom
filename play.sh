#!/bin/bash
# My first script

javac *.java

number=-27

while [ $number -lt 0 ]
do
  java Axiom
  number=$((number + 1))
done
