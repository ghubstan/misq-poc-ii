#!/bin/bash

# Prerequisite: Create bitcoin data dir `$ mkdir /tmp/regtest`

bitcoind -datadir=/tmp/regtest -regtest=1 -server=1 -txindex=1 -peerbloomfilters=1 -debug=net
