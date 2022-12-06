package ru.otus.crm.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "client")
    private Address address;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "client")
    private List<Phone> phoneList;


    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //TODO Прокомментируйте, пожалуйста! Такое просовывание клиента внутрь адреса и телефонов -
    // это какой-то адский костыль или стандартная практика???
    // без него в таблице в client_id ставятся null
    public Client(Long id, String name, Address address, List<Phone> phoneList) {
        this.id = id;
        this.name = name;
        setAddress(address);
        setPhoneList(phoneList);
    }

    public void setAddress(Address address) {
        if(address != null) {
            address.setClient(this);
        }
        this.address = address;
    }

    public void setPhoneList(List<Phone> phoneList) {
        if(phoneList!= null && phoneList.size() > 0) {
            phoneList.forEach(phone -> phone.setClient(this));
        }
        this.phoneList = phoneList;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phoneList);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
