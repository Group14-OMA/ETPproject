package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Input {



    private String instanceName;
    private Integer timeslots;
    private Integer studentNumber;
    private Integer examNumber;
    private Integer [] [] conflictMatrix;

    private ExecutorService executorService;

   

    public Input(String instanceName) {
        //The name of the istance must be used to get the 3 files
    this.instanceName=instanceName;
    studentNumber=0;



    }

    public void startInput() {
        TimeSlotsReader(instanceName+".slo");

        RandomExamReader(instanceName + ".exm");
        conflictMatrix =new Integer[examNumber][examNumber];
        StudentReader(instanceName + ".stu");
    }

    private void TimeSlotsReader(String file) {
        String line;
        Integer slots=0 ;
        try {
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(filereader);

            while ((line = bufferedreader.readLine()) != null) {
                if(line.matches("^[0-9]+")) {
                    slots = Integer.valueOf(line);
                    break;
                }
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

        timeslots=slots;
    }


    private void RandomExamReader(String fileName) {
        File file=new File(fileName);
        StringBuilder builder=new StringBuilder();
        try {
            RandomAccessFile randomAccessFile=new RandomAccessFile(file, "r");
            long fileLength=file.length()-1;
            boolean found=false;
            while(!found) {
                for (long pointer = fileLength; pointer >= 0; pointer--) {
                    randomAccessFile.seek(pointer);
                    char c;
                    // read from the last one char at the time
                    c = (char) randomAccessFile.read();
                    if (c == '\n') {
                        //builder.reverse();
                        if (!builder.toString().equals(""))

                            break;
                    }
                    if ((c != '\r') && (c != '\n'))
                        builder.append(c);
                }
                String reversed=builder.reverse().toString();
                if (reversed.matches("^[0-9]+\\s[0-9]+")) {
                    found = true;
                    String[] parts=reversed.split(" ");
                    examNumber=Integer.valueOf(parts[0]);

                }
                else {
                    builder.setLength(0); //clear the builder
                }
            }


        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
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

                    studentXExam[IndiceEsame].add(parts[0]);

                }

            //Costruisco la matrice
            matrixBuild(studentXExam);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    private void matrixBuild(LinkedList<String>[] studentXExam) {

       ArrayList< ParallelMatrixBuilder> pmthreds=new ArrayList<>();
        executorService = Executors.newCachedThreadPool();


        for(int y=0;y<examNumber;y++) {
            ParallelMatrixBuilder pm= new ParallelMatrixBuilder(y,studentXExam.clone());
            pmthreds.add(pm);
            executorService.submit(pm);



        }
        executorService.shutdown();


        try{
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        }catch (InterruptedException i){
            System.out.println(i.getMessage());
        }


        for(ParallelMatrixBuilder pl:pmthreds) {
            conflictMatrix[pl.getIndiceEsame()]=pl.getRow();

        }


    }




    public String getInstanceName() {
        return instanceName;
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
