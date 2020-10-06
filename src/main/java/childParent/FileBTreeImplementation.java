package childParent;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


public class FileBTreeImplementation implements Serializable, Cloneable {


    public static void main(String[] args) {
        List<DirNode> allFilesInTree = new FileBTreeImplementation("C:\\StorageUser\\dell\\Desktop\\childParent").getAllFilesInTree();

        allFilesInTree.forEach(System.out::println);
    }

    private String directory;


    public FileBTreeImplementation(String directory) {
        this.directory = directory;
    }

    public List<DirNode> getAllFilesInTree() {

        DirNode dirNode = new DirNode();
        dirNode.filePath = directory;

        try{
            binarySearch(dirNode, new File(directory), "childParent", dirNode.childArray);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return dirNode.childArray;
    }


    public static void binarySearch(DirNode parentNode, File directory, String userDirectory, List<DirNode> nodeArray) throws IOException {
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if(attrs.isDirectory()){
                    DirNode folderNode = new DirNode();
                    folderNode.parent = parentNode;
                    folderNode.filePath = dir.toString();
                    folderNode.isDirectory = true;

                    nodeArray.add(folderNode);


                } else {
                    DirNode fileNode = new DirNode();
                    fileNode.parent = parentNode;
                    fileNode.filePath = dir.toString();
                    fileNode.isDirectory = false;

                    nodeArray.add(fileNode);
                }

                return FileVisitResult.CONTINUE;
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





    public static class DirNode {

        List<DirNode> childArray;
        DirNode parent;

        //* Side fields *//
        boolean isDirectory;
        String filePath;

        public DirNode(){
            this.childArray = new ArrayList<>();
        }

    }





}
