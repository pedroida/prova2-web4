package br.edu.utfpr.hello.model.Product;

import br.edu.utfpr.hello.model.User.User;
import br.edu.utfpr.hello.structures.EntityApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "products")
public class Product extends EntityApplication {

    @NotEmpty(message = "Título é obrigatório")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Descrição é obrigatória")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Quantidade é obrigatória")
    @Column(name = "quantity")
    private int quantity;

    @NotNull(message = "Preço é obrigatório")
    @Column(name = "price")
    private double price;

    @NotNull(message = "Validade é obrigatória")
    @Column(name = "validity")
    private Date validity;

    @NotEmpty(message = "Unidade é obrigatória")
    @Column(name = "unit")
    private String unit;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Product(String title, String description, String quantity, String price, String validity, String unit) {
        this.setTitle(title);
        this.setDescription(description);

        if (quantity != null && !quantity.isEmpty())
            this.setQuantity(Integer.parseInt(quantity));

        this.setPrice(price);

        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        try {
            if (validity != null && !validity.isEmpty())
                this.setValidity(formatter.parse(validity));
        } catch (ParseException e) {
            this.setValidity(new Date());
        }

        this.setUnit(unit);
    }

    private void setPrice(String price) {
        this.price = Double.parseDouble(price
                .replace("R$", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", "."));
    }

    public String getFormattedPrice() {
        Locale ptBr = new Locale("pt", "BR");
        return NumberFormat.getCurrencyInstance(ptBr).format(this.price);
    }

    public String getFormattedValidity() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.validity);
    }
}
