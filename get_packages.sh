#!/bin/bash

##########################################################################
# Copyright (C) 2009 - 2011, Duncan Bayne & Armin Sadeghi
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of version 3 of the GNU Lesser General Public License 
# as published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be
# useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
# Public License for more details.
#
# The GNU Lesser General Public License can be viewed at
# http://www.gnu.org/licenses/lgpl-3.0.txt
#
# To find out more about ObzVault, visit
# https://github.com/asadeghi/obzvault
##########################################################################

#
# NOTE: This assumes Ubuntu 10.04.  Don't run in Emacs (Whiptail EULA).
#

sudo add-apt-repository "deb http://archive.canonical.com/ lucid partner"
sudo apt-get update

sudo apt-get install proguard sun-java6-bin sun-java6-jre sun-java6-jdk netbeans debhelper lintian ant -y
