package com.itechart.security.business.model.dto.helpers;

import com.itechart.security.business.model.dto.AttachmentDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentTempFileDto {
    private AttachmentDto attachment;
    private String filePath;
}
