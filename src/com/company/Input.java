package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class Input {



    private String instanceName;
    private Integer timeslots;
    private Integer studentNumber;
    private Integer examNumber;
    private Integer [] [] conflictMatrix;

   // private String

    public Input(String instanceName) {
        //The name of the istance must be used to get the 3 files
    this.instanceName=instanceName;
    studentNumber=0;
    timeslots =TimeSlotsReader(instanceName+".slo");
    examNumber =ExamReader(instanceName+".exm");
    conflictMatrix =new Integer[examNumber][examNumber];
    StudentReader(instanceName + ".stu");

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

    public String getInstanceName() {
        return instanceName;
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
            //Array di linked list per costruire Cee' , ogni lista va inizializzata
            LinkedList<String>[] studentXExam= new LinkedList[examNumber];
            String previousStudent="" ;



            //studentXExam init
            for(int j=0;j<examNumber;j++) studentXExam[j]=new LinkedList<>();

            try {

                FileReader filereader = new FileReader(file);
                BufferedReader bufferedreader = new BufferedReader(filereader);


                while ((line = bufferedreader.readLine()) != null) {
                   String parts[] =line.split(" ");
                   //Conto gli Studenti
                   if(!parts[0].equals(previousStudent)) {
                       studentNumber++;
                       previousStudent=parts[0];
                   }

                    /* Costruisco l'array degli studenti, aggiungo nella posizione x-1 (l'esame parte da 1) lo studente sX */


                    int IndiceEsame=(Integer.valueOf(parts[1]))-1;
                    System.out.println(parts[0]+"/" + parts[1] + "->" + IndiceEsame);
                    studentXExam[IndiceEsame].add(parts[0]);

                }

                for(int i = 0; i< examNumber; i++) {
                    for (int j = 0; j< examNumber; j++) {

                        if(i==j) conflictMatrix[i][j]=0;

                        //Molto poco ottimizzato, si può usare anche RetainAll
                        else {
                            //questo set è una copia del primo
                        Set<String> collisions=new HashSet<> (studentXExam[i]);
                        collisions.retainAll(studentXExam[j]); // mantiene solo gli elementi che ci sono in entrambe

                        conflictMatrix[i][j]=collisions.size();

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
        return conflictMatrix;
    }

    public Integer getTimeslots() {
        return timeslots;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public Integer getExamNumber() {
        return examNumber;
    }
}
