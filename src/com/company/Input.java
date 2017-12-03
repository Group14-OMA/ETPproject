package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class Input {




    private Integer Timeslots;
    private Integer StudentNumber;
    private Integer ExamNumber;
    private Integer [] [] ConflictMatrix;

   // private String

    public Input(String InstanceName) {
        //The name of the istance must be used to get the 3 files

    Timeslots=TimeSlotsReader(InstanceName+".slo");
    ExamNumber=ExamReader(InstanceName+".exm");
    ConflictMatrix=new Integer[ExamNumber][ExamNumber];
    StudentReader(InstanceName + ".stu");

    }


    private Integer TimeSlotsReader(String file) {
        String line;
        Integer slots=0 ;
        try {
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(filereader);

            while ((line = bufferedreader.readLine()) != null) {
                slots = Integer.valueOf(line);
            }



        } catch(FileNotFoundException fnf) {
            System.out.printf("No file %s found",file);
            System.exit(1);
        } catch(IOException ioe) {
            System.out.printf("IO error");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.out.printf("You did not insert a number in the file");
            System.exit(1);
        }

        return slots;
    }


    private Integer ExamReader(String file) {
        String line;
        Integer linecount = 0;

        try {
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(filereader);

            while ((line = bufferedreader.readLine()) != null) {
                linecount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linecount;
    }


    private void StudentReader(String file) {
            String line;
            LinkedList<String> [] StudentXExam= new LinkedList[ExamNumber]; //Array di linked list per costruire Cee'
            String PreviousStudent= null ;

            StudentNumber=0;

            try {

                FileReader filereader = new FileReader(file);
                BufferedReader bufferedreader = new BufferedReader(filereader);

                while ((line = bufferedreader.readLine()) != null) {
                   String parts[] =line.split(" ");
                   //Conto gli Studenti
                   if(!parts[0].equals(PreviousStudent)) {
                       StudentNumber++;
                       PreviousStudent=parts[0];
                   }

                    /* Costruisco l'array degli studenti, aggiungo nella posizione x-1 (l'esame parte da 1) lo studente sX */
                    int IndiceEsame=(Integer.valueOf(parts[1]))-1;
                    StudentXExam[IndiceEsame].add(parts[0]);

                }

                for(int i=0; i< ExamNumber ; i++) {
                    for (int j=0;j< ExamNumber; j++) {
                        //Essendo la matrice upperTriangular ho pensato di mettere 0 direttamente
                        if(i>=j) ConflictMatrix[i][j]=0;

                        //Molto poco ottimizzato, si può usare anche RetainAll
                        else {
                        Set<String> Collisions=new HashSet<String> ();
                        Collisions.addAll(StudentXExam[i]);
                        Collisions.addAll(StudentXExam[j]);
                        ConflictMatrix[i][j]=Collisions.size();

                        }

                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    public Integer[][] getConflictMatrix() {
        return ConflictMatrix;
    }

    public Integer getTimeslots() {
        return Timeslots;
    }

    public Integer getStudentNumber() {
        return StudentNumber;
    }

    public Integer getExamNumber() {
        return ExamNumber;
    }
}
