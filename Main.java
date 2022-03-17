package com.metanit;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.Arrays;
import java.io.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите имя, фамилию и отчество:\n");
        String name = sc.nextLine();
        System.out.println("Введите дату Вашего рождения:\n");
        String  birth = sc.nextLine();
        String[] n = name.split(" ");
        String s = "неопределён";
        char[] nameExit = new char[3];
        try {
            if (n.length != 3) {
                throw new IncorrectFormatException("Неправильное количество слов в вводе имени, фамилии и отчества. Их должно быть 3.");
            }
            for (int i = 0; i < 3; i++) {
                char[] charArray = n[i].toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    int k = (int) (charArray[j]);
                    if (j == 0) {
                        if (!(k>=1040 && k<=1071)) {
                           throw new  IncorrectFormatException("Проверьте первую букву каждого слова. Она должна быть заглавной.Язык:русский.");
                        }
                        nameExit[i] = charArray[j];
                    } else {
                        if (!(k>=1072 && k<=1103)) {
                            throw new  IncorrectFormatException("Проверьте формат ввода.Язык:русский.");
                        }
                        if (i == 2 && j == charArray.length - 1) {
                            if (!(k == 1072 || k == 1095)) {
                                throw new NoSException("Невозможно определить пол. Отчество введено неправильно");
                            }
                            if (k == 1095) {
                                s = "мужской";
                            } else {
                                s = "женский";
                            }
                        }
                    }
                }
            }
            String[] f = birth.split("\\.");
            if (f.length!=3){
                throw new WrongDateExeption("Неправильный формат даты.");
            }
            int[] dates = new int[3];
            try {
                for (int i = 0; i < 3; i++) {
                    dates[i] = Integer.parseInt(f[i]);
                }
            }
            catch(NumberFormatException x){
                throw new WrongDateExeption("Неправильный формат даты.");
            }
            long years;
            try{
                LocalDate start = LocalDate.of(dates[2], dates[1], dates[0]);
                LocalDate end = LocalDate.now();
                years = ChronoUnit.YEARS.between(start, end);
            }
            catch(java.time.DateTimeException x){
                throw new WrongDateExeption("Неправильный формат даты.");
            }
            System.out.println("ФИО:");
            System.out.println(nameExit[0]+"."+nameExit[1]+"."+nameExit[2]);
            System.out.println("Пол:");
            System.out.println(s);
            System.out.println("Возраст:");
            System.out.println(years);
        }
        catch(IncorrectFormatException x){
            System.out.println(x.getMessage());
        }
        catch(NoSException x){
            System.out.println(x.getMessage());
        }
        catch(WrongDateExeption x){
            System.out.println(x.getMessage());
        }
    }

}
