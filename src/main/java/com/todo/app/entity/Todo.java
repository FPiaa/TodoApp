package com.todo.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel(description = "Class representing a TODO in the application")
@Entity
@Table(name = "TODO")
@Getter
@Setter
public class Todo implements Serializable {

  @Serial
  private final static long serialVersionUID = 22465788L;

  @ApiModelProperty(notes = "Unique identifier of a TODO",
      example = "1",
      required = true
  )
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ApiModelProperty(notes = "TODO message",
      example = "Create this rest API",
      required = true,
      position = 1
  )
  @NotBlank
  @Size(max = 255)
  private String message;

  @ApiModelProperty(notes = "TODO status, can be done or not",
      example = "true",
      value = "false"
  )
  private boolean done;

  @ApiModelProperty(notes = "Creation timestamp for this TODO",
      example = "2022-05-24T19:21:27.836+00:00"
  )
  @CreationTimestamp
  private Timestamp createdAt;

  @ApiModelProperty(notes = "Last timestamp update for this TODO",
      example = "2022-05-24T19:21:42.532+00:00"
  )
  @UpdateTimestamp
  private Timestamp updatedAt;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;
}
