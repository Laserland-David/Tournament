package tournament;


import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;


public class ReadFiles {

	   public static void watchDirectoryPath(String pathToWatch, String pathToMove, GenerateJSON gJson) {
			// Sanity check - Check if path is a folder
			File dir = new File(pathToWatch);
			Path path = dir.toPath();
			
			
			try {
				Boolean isFolder = (Boolean) Files.getAttribute(path,
						"basic:isDirectory", NOFOLLOW_LINKS);
				if (!isFolder) {
					throw new IllegalArgumentException("Path: " + path
							+ " is not a folder");
				}
			} catch (IOException ioe) {
				// Folder does not exists
				ioe.printStackTrace();
			}
			System.out.println("Watching path: " + path);
			// We obtain the file system of the Path
			FileSystem fs = path.getFileSystem();
			// We create the new WatchService using the new try() block
			try (WatchService service = fs.newWatchService()) {
				// We register the path to the service
				// We watch for creation events
				path.register(service, ENTRY_CREATE);
				// Start the infinite polling loop
				WatchKey key = null;
				while (true) {
					key = service.take();
					// Dequeueing events
					Kind<?> kind = null;
					for (WatchEvent<?> watchEvent : key.pollEvents()) {
						// Get the type of the event
						kind = watchEvent.kind();
						if (OVERFLOW == kind) {
							continue; // loop
						} else if (ENTRY_CREATE == kind) {
							// A new Path was created
							Path fileName = ((WatchEvent<Path>) watchEvent).context();
							
							String PathWithNewFile = pathToWatch + "\\" + fileName.toString();
							String PathToMoveNewFile = pathToMove + "\\" + fileName.toString();
							
							// Output
							System.out.println("New File created: " + fileName);
							File actFile = new File(PathWithNewFile);
							ReadResult.read(actFile);
							
							//Path newPath = path + "" + fileName;
							System.out.println("Waiting 3 seconds...");
							  try {
						            Thread.sleep(3000); //keine schöne lösung aber laserfoerce hat das File noch im gebrauch...
						       } catch (InterruptedException e) {
						            e.printStackTrace();
						       }
							Files.move(Paths.get(PathWithNewFile), Paths.get(PathToMoveNewFile), StandardCopyOption.REPLACE_EXISTING);
							System.out.println("File finished -> move to: " + pathToMove + "\\" +fileName);
						}
					}
					if (!key.reset()) {
						break; // loop
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
}

