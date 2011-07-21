#!/bin/sh

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#
# Ldap Synchronization Connector provides tools to synchronize electronic
# identities from a list of data sources including any database with a JDBC
# connector, another LDAP directory, flat files...
#
# LSC automatically embed HSQLDB library. So, this script could :
# - Launch a HSQLDB server ;
# - Inject CSV data into it ;
# - Shutdown the HSQLDB server previously started.
#
# ---------------------------------------------------------------------------------
#
# Copyright (c) 2008 - 2011 LSC Project 
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without modification,
# are permitted provided that the following conditions are met:
#
#   * Redistributions of source code must retain the above copyright notice, this
#     list of conditions and the following disclaimer.
#   * Redistributions in binary form must reproduce the above copyright notice,
#     this list of conditions and the following disclaimer in the documentation
#     and/or other materials provided with the distribution.
#   * Neither the name of the LSC Project nor the names of its contributors may be
#     used to endorse or promote products derived from this software without
#     specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
# ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
# WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
# DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
# (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
# OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
# THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
# NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#
#        (c) 2008 - 2011 LSC Project
#   Sebastien Bahloul <seb@lsc-project.org>
#   Thomas Chemineau <thomas@lsc-project.org>
#   Jonathan Clarke <jon@lsc-project.org>
#   Remy-Christophe Schermesser <rcs@lsc-project.org>
#
# ---------------------------------------------------------------------------------
#
# Version 0.1 (2009):
# - First version
# Author: Thomas CHEMINEAU
#
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

# ---------------------------------------------------------------------------------
# CONFIGURATION
# ---------------------------------------------------------------------------------

# LSC home directory
LSC_HOME=/usr/local/lsc
# HSQLDB jar file
HSQLDB_LIB=$LSC_HOME/lib/hsqldb*.jar
# Temporary directory so that HSQLDB server could store files
HSQLDB_DIR=$LSC_HOME/var/hsqldb
# HSQLDB server PID file
HSQLDB_PIDFILE=$LSC_HOME/var/hsqldb.pid
# Default CSV separator
CSV_SEPARATOR=";"

# ---------------------------------------------------------------------------------
# FUNCTIONS
# ---------------------------------------------------------------------------------

#
# Drop all data from the HSQLDB instance.
#
data_drop()
{
  hsqldb_sqlQuery "DROP TABLE csvdata" 2> /dev/null
}

#
# Create the SQL table into the HSQLDB database. All field are read from the first
# line of the CSV file passed in the first parameter. Once the table is created,
# data should be correctly imported.
#
data_import()
{
  # $1 : CSV fullpath
  # $2 : CSV separator
  HSQLDB_TABLEDEF=""
  IFSO=$IFS
  IFS=$2
  for field in `head -n 1 $1`; do
    if [ -n "$HSQLDB_TABLEDEF" ]; then
      HSQLDB_TABLEDEF="$HSQLDB_TABLEDEF, $field VARCHAR"
    else
      HSQLDB_TABLEDEF="$field VARCHAR"
    fi
  done
  IFS=$IOFS
  if [ -n "$HSQLDB_TABLEDEF" ]; then
    hsqldb_sqlQuery "CREATE TEXT TABLE csvdata ($HSQLDB_TABLEDEF)"
    if [ $? -eq 0 ]; then
      nblines=`cat $1 | wc -l`
      let nblines--
      tail -n $nblines $1 > $HSQLDB_DIR/`basename $1`
      sep=`hsqldb_checkCsvSeparator $2`
      hsqldb_sqlQuery "SET TABLE csvdata SOURCE \"`basename $1`;fs=$sep\"" > /dev/null 2>&1
      echo $?
      rm -f $HSQLDB_DIR/`basename $1` > /dev/null 2>&1
      return
    fi
  fi
  echo 1
}

#
# Show data from the HSQLDB instance.
#
data_show()
{
  hsqldb_sqlQuery "SELECT * FROM csvdata" 2> /dev/null
}

#
# Check if the HSQLDB server is running or not. This function should check a
# process from the PIDFILE into the process list.
#
hsqldb_checkInstance()
{
  if [ ! -e $HSQLDB_PIDFILE ]; then
    echo 1
    return
  fi
  pid=`cat $HSQLDB_PIDFILE`
  if [ `ps -e | grep $pid | grep -v "grep $pid" | wc -l` -eq 0 ]; then
    echo 1
    return
  fi
  echo 0
}

#
# Return the CSV separator used by hsqldb.
#
hsqldb_checkCsvSeparator()
{
  # $1 : A separator
  case "$1" in
    ";")
      echo -n "\semi"
      ;;
    "\"")
      echo -n "\quote"
      ;;
    " ")
      echo -n "\space"
      ;;
    "'")
      echo -n "\apos"
      ;;
    *)
      echo -n $1
      ;;
  esac
}

#
# Remove all data on disk that have been created by an hsqldb server. No checks
# on a running process are performed, so be carefull.
#
hsqldb_cleanData()
{
  cd $HSQLDB_DIR > /dev/null 2>&1
  if [ $? -eq 0 ]; then
    rm -f *
    cd ..
    rmdir `basename $HSQLDB_DIR`
  fi
  rm -f $HSQLDB_PIDFILE > /dev/null 2>&1
}

#
# Perform a SQL query on the running HSQLDB instance. No checks on a running
# process are performed, so be carefull.
#
hsqldb_sqlQuery()
{
  # $1 : sql query
  java -jar $HSQLDB_LIB --rcFile $HSQLDB_DIR/hsqldb.rc --sql "$1" lscdb
}

#
# Start an instance of a HSQLDB server. No checks on a running process are
# performed, so be carefull. It will also build a RC file for futur auto
# authentication.
#
hsqldb_startServer()
{
  mkdir -p $HSQLDB_DIR
  java -cp $LSC_HOME/lib/hsqldb*.jar org.hsqldb.Server \
    -database.0 file:$HSQLDB_DIR/mydb -dbname.0 lscdb > /dev/null 2>&1 &
  echo $! > $HSQLDB_PIDFILE
  sleep 2
  if [ ! -d $HSQLDB_DIR ]; then
    mkdir $HSQLDB_DIR
  fi
  echo "
    urlid lscdb
    url jdbc:hsqldb:hsql://localhost/lscdb
    username sa
    password" > $HSQLDB_DIR/hsqldb.rc
}

#
# Hard stop the current instance of HSQLDB server. It means that it will send a
# SIGKILL on the process found through the PIDFILE. No checks on a running process
# are performed, so be carefull.
#
hsqldb_stopServer()
{
  kill -s KILL `cat $HSQLDB_PIDFILE`
}

# ---------------------------------------------------------------------------------
# MAIN
# ---------------------------------------------------------------------------------

# -- Check script parameters --

ACTIONS=""

case "$1" in
  "--drop")
    ACTIONS="$ACTIONS dropdata"
    shift
    ;;
  "--import")
    shift
    CSV_FILE="$1"
    shift
    if [ "$1" != "" ]; then
      CSV_SEPARATOR="$1"
      shift
    fi
    ACTIONS="$ACTIONS importdata"
    ;;
  "--show")
    ACTIONS="$ACTIONS showdata"
    shift
    ;;
  "--start")
    ACTIONS="$ACTIONS startserver"
    shift
    ;;
  "--stop")
    ACTIONS="$ACTIONS stopserver"
    shift
    ;;
  "--help"|*)
    echo ""
    echo " $0 [<option>]"
    echo ""
    echo " One option from available options is:"
    echo "   --import <file> [<separator>]    Import data from CSV file"
    echo "   --show    Show data from database"
    echo "   --start   Start the server"
    echo "   --stop    Stop the server"
    echo "   --help    Print informations"
    echo ""
    exit 1
    ;;
esac

# -- Run actions --

for action in $ACTIONS; do
  case "$action" in
    "dropdata")
      if [ `hsqldb_checkInstance` -eq 1 ]; then
        echo " Error - server is not running"
        exit 1
      fi
      data_drop
      ;;
    "importdata")
      if [ ! -e "$CSV_FILE" ]; then
        echo " Error - file '$1' not found"
        exit 1
      fi
      if [ `hsqldb_checkInstance` -eq 1 ]; then
        echo " Error - server is not running"
        exit 1
      fi
      exit `data_import "$CSV_FILE" "$CSV_SEPARATOR"`
      ;;
    "showdata")
      if [ `hsqldb_checkInstance` -eq 1 ]; then
        echo " Error - server is not running"
        exit 1
      fi
      data_show
      ;;
    "startserver")
      if [ `hsqldb_checkInstance` -eq 0 ]; then
        echo " Error - server already running"
        exit 1
      fi
      hsqldb_cleanData
      hsqldb_startServer
      ;;
    "stopserver")
      if [ `hsqldb_checkInstance` -eq 0 ]; then
        hsqldb_stopServer
        hsqldb_cleanData
      fi
      ;;

  esac
done

