package com.fad.fad.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FadApiService {

    private final RestTemplate restTemplate;

    @Value("${api_base_url}")
    private String apiBasePath;
    @Value("${api_endpoint_login}")
    private String endpointLogin;
    @Value("${api_endpoint_get_info}")
    private String endpointGetInfo;
    @Value("${api_endpoint_create_requisition}")
    private String endpointCreateRequisition;

    public Map<String, String> loginFAD() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("ZmFkLWMyYy1wb3J0YWw=",
                "MjhkN2Q3MmJiYTVmZGM0NmYxZjdkYWJjYmQ2NjA1ZDUzZTVhOWM1ZGU4NjAxNWUxODZkZWFiNzMwYTRmYzUyYg==");
        MultiValueMap<String, String> bodyData = new LinkedMultiValueMap<String, String>();
        bodyData.add("grant_type", "password");
        bodyData.add("username", "testft@na-at.com.mx");
        bodyData.add("password", "c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyData, headers);

        ResponseEntity<Map> petitionResponse = restTemplate
                .postForEntity(apiBasePath + endpointLogin, entity, Map.class);

        if (petitionResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("Peticion exitosa Token****************************");
            return petitionResponse.getBody();

        } else {
            System.out.println("****************************************Request Failed");
            System.out.println(petitionResponse.getStatusCode());
            return Collections.singletonMap("error", "true");

        }
    }

    public String getInfo(String id) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> loginResponse = loginFAD();
        System.out.println(loginResponse.get("access_token").toString());
        headers.setBearerAuth(loginResponse.get("access_token").toString());
        HttpEntity request = new HttpEntity(headers);

        System.out.println(apiBasePath + endpointGetInfo + id);
        ResponseEntity<String> petitionResponse = restTemplate.exchange(apiBasePath + endpointGetInfo + id,
                HttpMethod.GET, request,
                String.class);

        if (petitionResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("Peticion exitosa****************************");
            System.out.println(petitionResponse.getBody());
            return petitionResponse.getBody();
        } else {
            System.out.println("****************************************Request Failed");
            System.out.println(petitionResponse.getStatusCode());
            System.out.println(petitionResponse.getBody());
            return "\"error\": \"true\"";
        }

    }

    public String createSignature(String name, String email, String phone) {

        int modifyXMLSuccess = modifyXMLFile(name, email, phone);
        if (modifyXMLSuccess == 1) {

            System.out.println("Inicio de firma");
            HttpHeaders headers = new HttpHeaders();
            Map<String, String> loginResponse = loginFAD();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(loginResponse.get("access_token").toString());
            MultiValueMap<String, Object> bodyData = new LinkedMultiValueMap<String, Object>();
            bodyData.add("xml", new FileSystemResource(new File("src/main/resources/static/files/sample_xml_web.xml")));
            bodyData.add("pdf", new FileSystemResource(new File("src/main/resources/static/files/sample_pdf.pdf")));
            bodyData.add("hash", "a9446ff41675be3f153e121d1fd34ac6adde245612a0d3ff9e279662969050b2");
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(bodyData, headers);
            ResponseEntity<String> petitionResponse = restTemplate.exchange(
                    "https://uat.firmaautografa.com/requisitions/createRequisicionB2C", HttpMethod.POST, entity,
                    String.class);
            if (petitionResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("Peticion exitosa****************************");
                System.out.println(petitionResponse.getBody());
                return petitionResponse.getBody();
            } else {
                System.out.println("****************************************Request Failed");
                System.out.println(petitionResponse.getStatusCode());
                System.out.println(petitionResponse.getBody());
                return ("{error\", \"true}");
            }
        } else {
            return ("{error\", \"true}");
        }

    }

    public int modifyXMLFile(String name, String email, String phone) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("src/main/resources/static/files/sample_xml_web.xml");
            String date = DateTimeFormatter.ofPattern("MMM dd yyyy").format(LocalDateTime.now());
            String textAccept = "Yo " + name + " acepto firmar Contrato Banca, hoy " + date + ".";

            Node node = document.getElementsByTagName("signerName").item(0);
            node.setTextContent(name);
            node = document.getElementsByTagName("acceptanceLegend").item(0);
            node.setTextContent(textAccept);

            node = document.getElementsByTagName("mail").item(0);
            node.setTextContent(email);

            node = document.getElementsByTagName("phone").item(0);
            node.setTextContent(phone);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult("src/main/resources/static/files/sample_xml_web.xml"));
            System.out.println("Cambio de XML");
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }

}
