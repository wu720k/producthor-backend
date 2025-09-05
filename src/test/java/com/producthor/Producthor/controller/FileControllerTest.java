package com.producthor.Producthor.controller;

import com.producthor.Producthor.rest.controller.FileController;
import com.producthor.Producthor.service.file.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FileController.class)
class FileControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private FileStorageService storage;

    @Test
    void upload_returns_public_url() throws Exception {
        var file = new MockMultipartFile("file", "pic.jpg", "image/jpeg", new byte[]{1,2,3});
        Mockito.when(storage.store(Mockito.any())).thenReturn("/files/abc.jpg");

        mvc.perform(multipart("/api/file/admin/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("/files/abc.jpg"));
    }
}
