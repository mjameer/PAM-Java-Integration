package com.pam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PAMResponseDTO {

    @JsonProperty("Content")
    private String content;

    @JsonProperty("CreationMethod")
    private String creationMethod;

    @JsonProperty("LastTask")
    private String lastTask;

    @JsonProperty("RetriesCount")
    private String retriesCount;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("CPMStatus")
    private String cpmStatus;

    @JsonProperty("Safe")
    private String safe;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("PolicyID")
    private String policyID;

    @JsonProperty("DeviceType")
    private String deviceType;

    @JsonProperty("Folder")
    private String folder;

    @JsonProperty("LastSuccessVerification")
    private String lastSuccessVerification;

    @JsonProperty("PasswordChangeInProcess")
    private String passwordChangeInProcess;

}