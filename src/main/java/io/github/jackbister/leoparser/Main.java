package io.github.jackbister.leoparser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class Main {
    public static void main(String args[]) throws java.io.IOException, IllegalArgumentException {
        InputStream input = System.in;
        if (Arrays.asList(args).contains("-i")) {
            int fileNameIdx = Arrays.asList(args).indexOf("-i") + 1;
            if (fileNameIdx >= args.length) {
                throw new IllegalArgumentException("-i must be followed by a file name.");
            }
            input = new FileInputStream(args[fileNameIdx]);
        }
        BufferedImage output = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
        try {
            Lexer lexer = new Lexer(input);
            if (lexer.hasMoreTokens()) {
                Leonardo leo = new Leonardo(output.getGraphics(), output.getWidth(), output.getHeight());
                Parser parser = new Parser(lexer);
                CmdList result;
                result = parser.parse();
                result.process(leo);
            } else {
                System.out.println("Empty input file.");
            }
        } catch (SyntaxError e) {
            System.out.println("Syntax error on line: " + e.getLine());
            //Stack trace which can help figure out where the syntax error was.
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        File outputFile = null;
        if (Arrays.asList(args).contains("-o")) {
            int fileNameIdx = Arrays.asList(args).indexOf("-o") + 1;
            if (fileNameIdx >= args.length) {
                throw new IllegalArgumentException("-o must be followed by a file name.");
            }
            outputFile = new File(args[fileNameIdx]);
        } else {
            outputFile = new File("a.png");
        }
        ImageIO.write(output, "png", outputFile);
    }
}
