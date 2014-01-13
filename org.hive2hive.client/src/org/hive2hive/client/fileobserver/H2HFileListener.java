package org.hive2hive.client.fileobserver;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.hive2hive.core.IH2HNode;
import org.hive2hive.core.exceptions.IllegalFileLocation;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.core.log.H2HLogger;
import org.hive2hive.core.log.H2HLoggerFactory;

public class H2HFileListener implements FileAlterationListener {
	
	private static final H2HLogger logger = H2HLoggerFactory.getLogger(H2HFileListener.class);

	private final IH2HNode node;
	
	public H2HFileListener(IH2HNode node) {
		this.node = node;
	}
	
	@Override
	public void onStart(FileAlterationObserver observer) {
	}

	@Override
	public void onDirectoryCreate(File directory) {
		printFileDetails("created", directory);
		addFile(directory);
	}

	@Override
	public void onDirectoryChange(File directory) {
		printFileDetails("changed", directory);
		// TODO implement onDirectoryChange
	}

	@Override
	public void onDirectoryDelete(File directory) {
		printFileDetails("deleted", directory);
		removeFile(directory);
	}

	@Override
	public void onFileCreate(File file) {
		printFileDetails("created", file);
		addFile(file);
	}

	@Override
	public void onFileChange(File file) {
		printFileDetails("changed", file);
		// TODO implement onFileChange
	}

	@Override
	public void onFileDelete(File file) {
		printFileDetails("deleted", file);
		removeFile(file);
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
	}

	private void addFile(File file){
		try {
			node.getFileManagement().add(file);
		} catch (IllegalFileLocation | NoSessionException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void removeFile(File file){
		try {
			node.getFileManagement().delete(file);
		} catch (IllegalArgumentException | NoSessionException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void printFileDetails(String reason, File file) {
		logger.debug(String.format("%s %s: %s\n", file.isDirectory() ? "Directory" : "File", reason, file.getAbsolutePath()));
	}
}