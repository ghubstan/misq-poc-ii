#!/bin/bash

kill $(ps aux | grep 'MisqAppMain' | awk '{print $2}')