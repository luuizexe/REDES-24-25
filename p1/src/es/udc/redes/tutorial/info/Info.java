package es.udc.redes.tutorial.info;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.*;

public class Info {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("uso: Info <ruta relativa>");
            return;
        }
        Path pathRel = Paths.get(args[0]);
        Path pathAbs = pathRel.toAbsolutePath();
        String nombre = pathAbs.getFileName().toString();
        int lastDotIndex = nombre.lastIndexOf('.');
        String extension = "";
        if (lastDotIndex > 0) {
            extension = nombre.substring(lastDotIndex + 1);
        }
        System.out.println("Tamaño: " + Files.size(pathAbs) + " bytes");
        System.out.println("Fecha de ultima modificacion:" + Files.getLastModifiedTime(pathAbs));
        System.out.println("Nombre:" + nombre);
        System.out.println("Extension:" + extension);
        System.out.println("Tipo de fichero:" + Files.probeContentType(pathAbs));
        System.out.println("Ruta absoluta:" + pathAbs);

    }

}
