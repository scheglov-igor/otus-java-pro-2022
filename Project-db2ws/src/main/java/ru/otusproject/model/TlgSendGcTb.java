package ru.otusproject.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("tlg_send_gc_tb")
@Getter
@Setter
@ToString
public class TlgSendGcTb {

    @Id
    private Long id;
    private String tlgText;
    private Integer tlgStatus;

    private String tlgWebservice;

    private String tlgResult;

    private Long gcSasId;
    private Integer gcSasErrorcode;
    private String gcSasErrorMessage;

    private Integer rowState;
    private LocalDateTime dateChange;

}
