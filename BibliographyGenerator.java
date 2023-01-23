import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class BibliographyGenerator {


    /**
     *
     * @param author the String of authors collected from the .bib file
     * @return a String with the authors in the IEEE citation format
     */
    public static String authorInIEEE(String author) {
        String[] ieeeAuthorArray = author.split(" ");
        String ieeeAuthor = "";


        int count = 0;
        for (String e : ieeeAuthorArray) {
            if (e.equals("and")) {
                ieeeAuthor += ",";
            } else {
                if (count == 0) {
                    ieeeAuthor += e;
                    count++;
                } else {
                    ieeeAuthor += " " + e;
                }
            }
        }
        return ieeeAuthor;
    }


    /**
     *
     * @param author the String of authors collected from the .bib file
     * @return a String with the authors in the NJ citation format
     */
    public static String authorInNJ(String author) {
        String[] njAuthorArray = author.split(" ");
        String njAuthor = "";
        int count = 0;
        for (String e : njAuthorArray) {
            if (e.equals("and")) {
                njAuthor += " &";
            } else {
                if (count == 0) {
                    njAuthor += e;
                    count++;
                } else {
                    njAuthor += " " + e;
                }
            }
        }
        return njAuthor;
    }


    /**
     *
     * @param author a String with the authors in the IEEE citation format
     * @return a String with the authors in the ACM citation format
     */
    public static String authorInACM(String author) {
        String[] acmAuthorArray = author.split(" ");
        String acmAuthor = "";
        int count = 0;
        boolean moreThanOneAuthor = false;
        for (String e : acmAuthorArray) {
            if (e.equals("and")) {
                moreThanOneAuthor = true;
                break;
            } else {
                if (count == 0) {
                    acmAuthor += e;
                    count++;
                } else {
                    acmAuthor += " " + e;
                }
            }
        }


        if (moreThanOneAuthor) {
            acmAuthor += " et al.";
        }

        return acmAuthor;
    }


    /**
     *
     * @param sc The scanner object with the .bib file open
     * @param acm a Printwriter object for writing to a acm file
     * @param ieee a Printwriter object for writing to a ieee file
     * @param nj a Printwriter object for writing to a nj file
     * @param i the index of the array to traverse through the scanner and printwriter arrays
     * @return
     */
    public static boolean processFilesforValidation(Scanner[] sc, PrintWriter[] acm, PrintWriter[] ieee, PrintWriter[] nj, int i) {

        try {
            while (sc[i].hasNextLine()) {


                String outerIteration = sc[i].nextLine();

                String author = null;
                String journal = null;
                String title = null;
                String year = null;
                String volume = null;
                String number = null;
                String pages = null;
                String keywords = null;
                String doi = null;
                String ISSN = null;
                String month = null;






                if (outerIteration.contains("@ARTICLE")) {



                    while (true) {

                        String innerIteration = sc[i].nextLine();

                        if (innerIteration.contains("={}")) {
                            String field;
                            field = innerIteration.substring(0, innerIteration.indexOf("="));
                            throw new FileInvalidException("Error: Detected Empty Field. Problem detected with input file Latex" + (i + 1) + ".bib" + "\nFile is invalid: field " + field + " is empty. Processing stopped at this point. Other empty fields may be present as well!");
                        }
                        if (innerIteration.contains("author={")) {
                            author = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("journal={")) {
                            journal = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("title={")) {
                            title = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("year={")) {
                            year = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("volume={")) {
                            volume = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("number={")) {
                            number = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("pages={")) {
                            pages = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("keywords={")) {
                            keywords = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("doi")) {
                            doi = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("ISSN={")) {
                            ISSN = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.contains("month={")) {
                            month = innerIteration.substring(innerIteration.indexOf("{") + 1, innerIteration.indexOf("}"));
                        } else if (innerIteration.equals("}")) {
                            break;
                        }
                    }
                    ieee[i].println(authorInIEEE(author) + "." + " \"" + title + "\", " + journal + ", vol. " + volume + ", no. " + number + ", p. " + pages + ", " + month + " " + year + ".");
                    nj[i].println(authorInNJ(author) + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").");
                    acm[i].println(authorInACM(author) + " " +year+ " "+ title + ". " + journal + ". " + volume + ", " + number + " " + "(" + year + ")" + ", " + pages + ". " + "DOI:" + doi + ".");

                }

            }

            acm[i].close();
            ieee[i].close();
            nj[i].close();

        } catch (FileInvalidException e) {
            System.out.println(e.getMessage());
            acm[i].close();
            ieee[i].close();
            nj[i].close();

            File f1 = new File("ACM" + (i + 1) + ".json");
            File f2 = new File("IEEE" + (i + 1) + ".json");
            File f3 = new File("NJ" + (i + 1) + ".json");
            f1.delete();
            f2.delete();
            f3.delete();
            return false;
        }

        return true;
    }

}

