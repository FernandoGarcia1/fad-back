package com.fad.fad.controllers;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fad.fad.domain.SignatureProcesses;
import com.fad.fad.model.HttpResponse;
import com.fad.fad.model.RequisitionId;
import com.fad.fad.model.Signatures;
import com.fad.fad.model.UserModel;
import com.fad.fad.service.FadApiService;
import com.fad.fad.service.SignatureProcessesService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/fad")
public class FadController {

    @Autowired
    SignatureProcessesService signatureProcessesService;
    final FadApiService fadApiService;

    // @RequestMapping(value = "/signature", method = RequestMethod.POST, produces =
    // MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/signature")
    public HttpResponse createSignature(@RequestBody Signatures signature) throws IOException, JSONException {
        // UserModel userModel = (UserModel)
        // SecurityContextHolder.getContext().getAuthentication();
        String response = fadApiService.createSignature(signature.getName(), signature.getEmail(),
                signature.getPhone());
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonReturn = new JSONObject();
        SignatureProcesses save = new SignatureProcesses();
        save.setRequisition_id(jsonResponse.getJSONObject("data").getString("requisitionId"));
        save.setUrl(jsonResponse.getJSONObject("data").getString("url"));
        save.setTicket(jsonResponse.getJSONObject("data").getString("ticket"));
        HttpResponse returnResponse = new HttpResponse();
        try {
            save = signatureProcessesService.save(save);
            jsonReturn.put("status", "OK");
            returnResponse.setStatus("OK");
            return returnResponse;
        } catch (Exception e1) {

            returnResponse.setStatus("error");
            return returnResponse;

        }

    }

    @GetMapping("/signature")
    public List<SignatureProcesses> listSignatureProcesses() {
        // UserModel userModel = (UserModel)
        // SecurityContextHolder.getContext().getAuthentication();
        return signatureProcessesService.findAll();
    }

    // @PostMapping("/signature-details")
    @RequestMapping(value = "/signature-details", method = RequestMethod.POST, produces = "application/json")
    public String getSignatureDetails(@RequestBody RequisitionId id) throws JSONException {
        String infoResponse = fadApiService.getInfo(id.getId());
        JSONObject jsonObject = new JSONObject(infoResponse);
        System.out.println(jsonObject.getString("success"));
        return infoResponse;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() throws IOException {
        // UserModel userModel = (UserModel)
        // SecurityContextHolder.getContext().getAuthentication();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", "sss");
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<>("{\"status\" : \"error\"}", HttpStatus.OK);
        }

    }

    @GetMapping("/test2")
    public void test2() {
        // UserModel userModel = (UserModel)
        // SecurityContextHolder.getContext().getAuthentication();
        try {
            System.out.println("File Modified");
            // FadApiService.modifyXMLFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
