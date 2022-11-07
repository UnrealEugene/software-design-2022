package ru.akirakozov.sd.refactoring.util;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;

public class ResponseHtmlUtil {
    public static String productToHtml(String caption, Product product) {
        return String.format(
                "<html><body><h1>%s: %s\t%s</br></body></html>",
                caption,
                product.getName(),
                product.getPrice()
        );
    }

    public static String productsToHtml(List<Product> products) {
        StringBuilder sb = new StringBuilder("<html><body>");
        for (Product product : products) {
            sb.append(product.getName())
                    .append("\t")
                    .append(product.getPrice())
                    .append("</br>");
        }
        return sb.append("</body></html>").toString();
    }

    public static String statisticToHtml(String caption, Object value) {
        return new StringBuilder("<html><body>")
                .append(caption)
                .append(": ")
                .append(value.toString())
                .append("</body></html>")
                .toString();
    }
}
