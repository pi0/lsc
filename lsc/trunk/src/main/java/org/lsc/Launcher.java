/*
 ****************************************************************************
 * Ldap Synchronization Connector provides tools to synchronize
 * electronic identities from a list of data sources including
 * any database with a JDBC connector, another LDAP directory,
 * flat files...
 *
 *                  ==LICENSE NOTICE==
 * 
 * Copyright (c) 2008, LSC Project 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:

 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of the LSC Project nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *                  ==LICENSE NOTICE==
 *
 *               (c) 2008 - 2009 LSC Project
 *         Sebastien Bahloul <seb@lsc-project.org>
 *         Thomas Chemineau <thomas@lsc-project.org>
 *         Jonathan Clarke <jon@lsc-project.org>
 *         Remy-Christophe Schermesser <rcs@lsc-project.org>
 ****************************************************************************
 */
package org.lsc;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main launching class This is the main wrapper for generic launcher.
 * This class is responsible of parameters analysis
 *
 * @author S. Bahloul &lt;seb@lsc-project.org&gt;
 */
public final class Launcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

	/** List of the synchronizing types. */
	private List<String> syncType;

	/** List of the asynchronous synchronizing types. */
	private List<String> asyncType;

	/** List of the cleaning types. */
	private List<String> cleanType;

	/** Configuration files location */
	private String configurationLocation;

	/** Default synchronize instance. */
	private SimpleSynchronize sync;

	/** Number of parallel threads to run a task */
	private int threads;

	/** Time limit in seconds*/
	private int timeLimit;
	
	/**
	 * Default constructor - instantiate objects.
	 */
	public Launcher() {
		syncType = new ArrayList<String>();
		asyncType = new ArrayList<String>();
		cleanType = new ArrayList<String>();
		sync = new SimpleSynchronize();
	}

	/**
	 * Main launcher.
	 *
	 * @param args parameters passed by the JRE
	 *
	 * @throws MalformedURLException thrown
	 */
	public static void main(final String[] args) throws MalformedURLException {
		// Create the object and parse options
		Launcher obj = new Launcher();
		int retCode = obj.parseOptions(args);

		if (retCode != 0) {
			System.exit(retCode);
		}

		// Wrap the launcher
		obj.run();
	}

	/**
	 * Launch the synchronization and cleaning process.
	 */
	public void run() {
		try {
			// if a configuration directory was set on command line, use it to set up Configuration
			Configuration.setUp(configurationLocation);

			// do the work!
			if (threads > 0) {
				sync.setThreads( threads );
			}
			if (timeLimit > 0) {
				sync.setTimeLimit( timeLimit );
			}
			sync.launch(asyncType, syncType, cleanType);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			LOGGER.debug(e.toString(), e);
		}
	}

	/**
	 * Manage command line options.
	 * @param args command line
	 * @return the status code (0: OK, >=1 : failed)
	 */
	private int parseOptions(final String[] args) {
		Options options = sync.getOptions();
		options.addOption("nc", "nocreate", false, "Don't create any entry");
		options.addOption("nu", "noupdate", false, "Don't update");
		options.addOption("nd", "nodelete", false, "Don't delete");
		options.addOption("nr", "nomodrdn", false, "Don't rename (MODRDN)");
		options.addOption("n", "dryrun", false,
				"Don't update the directory at all");
		options.addOption("l", "startLdapServer", false,
						"Start the embedded OpenDS LDAP server (will be shutdown at the end)");
		options.addOption("a", "synchronization", true,
						"Asynchronous synchronization type (one of the available tasks or 'all')");
		options.addOption("s", "synchronization", true,
						"Synchronization type (one of the available tasks or 'all')");
		options.addOption("c", "cleaning", true,
						"Cleaning type (one of the available tasks or 'all')");
		options.addOption("f", "cfg", true,
						"Specify configuration directory");
		options.addOption("t", "number of parrallel threads to synchronize a task", true,
						"Server mode");
		options.addOption("i", "Time limit in parallel server mode", true, "Time limit");
		options.addOption("h", "help", false, "Get this text");

		CommandLineParser parser = new GnuParser();

		try {
			CommandLine cmdLine = parser.parse(options, args);

			if (cmdLine.hasOption("s")) {
				asyncType = parseSyncType(cmdLine.getOptionValue("a"));
			}
			if (cmdLine.hasOption("s")) {
				syncType = parseSyncType(cmdLine.getOptionValue("s"));
			}
			if (cmdLine.hasOption("f")) {
				configurationLocation = new File(cmdLine.getOptionValue("f")).getAbsolutePath();
			}
			if (cmdLine.hasOption("t")) {
				threads = Integer.parseInt(cmdLine.getOptionValue("t"));
			}
			if (cmdLine.hasOption("t")) {
				threads = Integer.parseInt(cmdLine.getOptionValue("t"));
			}
			if (cmdLine.hasOption("i")) {
				timeLimit = Integer.parseInt(cmdLine.getOptionValue("i"));
			}
			if (cmdLine.hasOption("c")) {
				cleanType = parseSyncType(cmdLine.getOptionValue("c"));
			}
		
			if(cmdLine.getOptions().length == 0 || 
							cmdLine.hasOption("h") || 
							!sync.parseOptions(cmdLine) ||
							((syncType.size() == 0) && (cleanType.size() == 0))) {
				printHelp(options);
				return 1;
			}
			if(!asyncType.isEmpty() && (!syncType.isEmpty() || !cleanType.isEmpty())) {
				System.err.println("Asynchronous synchronization is mutually exclusive with synchronous synchronizing and cleaning !");
				printHelp(options);
				return 1;
			}
		} catch (ParseException e) {
			LOGGER.error("Unable to parse the options ({})", e.toString());
			LOGGER.debug(e.toString(), e);
			return 1;
		}
		return 0;
	}

	/**
	 * Parse the synchronization string to find the right type of
	 * synchronization or cleaning.
	 * @param syncValue the string comma separated synchronization name
	 * @return the synchronizations name
	 */
	private List<String> parseSyncType(final String syncValue) {
		List<String> ret = new ArrayList<String>();

		// Add each value to returned strings list
		StringTokenizer st = new StringTokenizer(syncValue, ",");

		while (st.hasMoreTokens()) {
			ret.add(st.nextToken());
		}

		return ret;
	}

	/**
	 * Print the command line help.
	 * @param options specified options to manage
	 */
	private static void printHelp(final Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("lsc", options);
	}
}