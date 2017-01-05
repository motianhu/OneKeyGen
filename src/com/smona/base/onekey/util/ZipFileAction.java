package com.smona.base.onekey.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipFileAction {
    public void zip(String inputFileName, String outputFileName)
            throws Exception {
        OutputStream output = new FileOutputStream(outputFileName);
        CheckedOutputStream stream = new CheckedOutputStream(output,
                new CRC32());
        ZipOutputStream out = new ZipOutputStream(stream);
        compress(out, new File(inputFileName), "");
        out.closeEntry();
        out.close();
    }

    private void compress(ZipOutputStream out, File file, String basedir) {
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            println("zip: " + file.getAbsolutePath() + ", basedir: " + basedir);
            compressFile(file, out, basedir);
        }
    }

    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        basedir = basedir.length() == 0 ? "" : basedir + File.separator;
        for (int i = 0; i < files.length; i++) {
            compress(out, files[i], basedir + files[i].getName());
        }
    }

    private void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir);
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[1024];
            while ((count = bis.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* unzip */
    public void unZip(String zipFileName, String imageOutput, String xmlOutput)
            throws Exception {
        println("unZip: " + zipFileName);
        ZipFile zipFile = new ZipFile(zipFileName);
        try {
            Enumeration<?> e = zipFile.getEntries();
            ZipEntry zipEntry = null;
            createDirectory(imageOutput, "");
            createDirectory(xmlOutput, "");
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                String fileName = zipEntry.getName();
                println("unZip: " + fileName);
                File f = null;
                if (fileName.endsWith(Constants.XML) || fileName.endsWith(Constants.PROPERTIES)) {
                    f = new File(xmlOutput + File.separator
                            + zipEntry.getName());
                } else {
                    f = new File(imageOutput + File.separator
                            + zipEntry.getName());
                }
                f.createNewFile();
                InputStream in = zipFile.getInputStream(zipEntry);
                FileOutputStream out = new FileOutputStream(f);
                byte[] by = new byte[8192];
                int c;
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
                in.close();
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            println(ex.getMessage());
        } finally {
            zipFile.close();
        }
    }

    private void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true) {
                fl.mkdir();
            } else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            println(ex.getMessage());
        }
    }

    private void println(String msg) {
        Logger.printDetail(msg);
    }
}
