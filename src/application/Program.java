package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import entities.Sale;

public class Program {

    public static void main(String[] args) {
    	Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entre o caminho do arquivo: ");
        String path = scanner.nextLine();
        
        List<Sale> sales = readSalesFile(path);

        if (sales != null) {
            System.out.println("\nCinco primeiras vendas de 2016 de maior pre�o m�dio");
            sales.stream()
                 .filter(sale -> sale.getYear() == 2016)
                 .sorted(Comparator.comparing(Sale::averagePrice).reversed())
                 .limit(5)
                 .forEach(System.out::println);
            
            double total = sales.stream()
                                .filter(sale -> sale.getSeller().equals("Logan") && (sale.getMonth() == 1 || sale.getMonth() == 7))
                                .mapToDouble(Sale::getTotal)
                                .sum();

            System.out.printf("\nValor total vendido pelo vendedor Logan nos meses 1 e 7 = %.2f", total);
            
            scanner.close();
        }
    }

    public static List<Sale> readSalesFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines()
                         .map(line -> {
                            String[] fields = line.split(",");
                            return new Sale(
                                    Integer.parseInt(fields[0]),
                                    Integer.parseInt(fields[1]),
                                    fields[2],
                                    Integer.parseInt(fields[3]),
                                    Double.parseDouble(fields[4])
                            );
                         })
                         .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro: formato inv�lido de n�mero");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Erro: linha com n�mero inv�lido de campos");
        }
        return null;
    }
}
