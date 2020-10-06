package childParent;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChildParentTest {

    private static List<DirNode> dirFiles = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {


        File file = new File("C:\\StorageUser\\dell\\Desktop\\childParent");

//        filesBinaryTree(file);
        betterBinaryTree(file, "childParent");

        dirFiles.forEach(value -> {
            System.out.println(value.parent.path + " ||| " + value.path);
        });


    }

    /*
      @
     */

    public static List<FileNode>getAllDirectoryFiles(File directory, DirNode parent, String userDirectory) {
        List<FileNode> files = new ArrayList<>();

        for(File file : Objects.requireNonNull(directory.listFiles())) {
                if(file.isDirectory()) {
                    DirNode dirNode = new DirNode();
                    dirNode.path = file.getPath();
                    dirNode.childNodes = getAllDirectoryFiles(file, dirNode, userDirectory);

                    DirNode parentNode = new DirNode();

                    if(file.getParent().toString().contains(userDirectory)){
                        parentNode.path = file.getParent().toString();
                    }

                    dirNode.parent = parentNode;

                    dirFiles.add(dirNode);
                } else {
                    FileNode node = new FileNode();
                    node.path = file.getPath();
                    node.parent = parent;
                    files.add(node);
                }

        }

        return files;
    }

    public static void betterBinaryTree(File directory, String userDirectory) throws IOException {
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if(attrs.isDirectory()) {
                    DirNode dirNode = new DirNode();
                    dirNode.path = dir.toString();
                    dirNode.childNodes = getAllDirectoryFiles(dir.toFile(), dirNode, userDirectory);

                    DirNode parentNode = new DirNode();

                    if(dir.getParent().toString().contains(userDirectory)){
                        parentNode.path = dir.getParent().toString();
                    }

                    dirNode.parent = parentNode;

                    dirFiles.add(dirNode);
                }

                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

            public static class FileNode   {
                DirNode parent;
                DirNode current;
                String path;

            }


            public static class DirNode implements Comparable<DirNode> {
                List<FileNode> childNodes;
                DirNode parent;
                String path;

             @Override
             public int compareTo(DirNode dirNode) {
                if(this.parent != dirNode.parent){
                    return 1;
                } else {
                    return 0;
                }
         }
     }



}
