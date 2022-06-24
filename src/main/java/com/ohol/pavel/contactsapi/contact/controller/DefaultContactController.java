package com.ohol.pavel.contactsapi.contact.controller;

import com.ohol.pavel.contactsapi.contact.dto.ContactDTO;
import com.ohol.pavel.contactsapi.contact.dto.ContactRequest;
import com.ohol.pavel.contactsapi.contact.service.ContactService;
import com.ohol.pavel.contactsapi.rest.RestConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author Pavel Ohol
 */
@RestController
@CrossOrigin
@Api(tags = "contacts-controller")
@AllArgsConstructor
public class DefaultContactController implements ContactController {

    /**
     * Contact service.
     */
    private final ContactService theContactService;

    /**
     * Conversion service.
     */
    private final ConversionService theConversionService;

    @Override
    @GetMapping(path = RestConstants.Contact.CONTACTS_ROOT, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('contact:read')")
    @ApiOperation("Find all contacts.")
    public ResponseEntity<Page<ContactDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(theContactService.findAll(pageable).map(
                p -> theConversionService.convert(p, ContactDTO.class)
        ));
    }

    @Override
    @GetMapping(path = RestConstants.Contact.CONTACT, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('contact:read')")
    @ApiOperation("Find contact by id.")
    public ResponseEntity<ContactDTO> findById(@PathVariable String contactId) {
        return ResponseEntity.ok(theConversionService.convert(
                theContactService.findById(contactId), ContactDTO.class
        ));
    }

    @Override
    @PostMapping(path = RestConstants.Contact.CONTACTS_ROOT,
            consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE},
            produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Save new contact.")
    @PreAuthorize("hasAuthority('contact:write')")
    public ResponseEntity<ContactDTO> save(
            @RequestPart ContactRequest request,
            @RequestPart MultipartFile file) {
        return new ResponseEntity<>(
                theConversionService.convert(theContactService.create(request, file), ContactDTO.class), CREATED);
    }

    @Override
    @PutMapping(path = RestConstants.Contact.CONTACT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Update contact.")
    @PreAuthorize("hasAuthority('contact:write')")
    public ResponseEntity<ContactDTO> update(@PathVariable String contactId,
                                             @RequestBody ContactRequest request) {
        return ResponseEntity.ok(theConversionService
                .convert(theContactService.update(contactId, request), ContactDTO.class));
    }

    @Override
    @PutMapping(path = RestConstants.Contact.UPDATE_CONTACT_AND_IMAGE,
            consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE},
            produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Update contact.")
    @PreAuthorize("hasAuthority('contact:write')")
    public ResponseEntity<ContactDTO> updateContactAndImage(@PathVariable String contactId,
                                             @RequestPart MultipartFile file,
                                             @RequestPart ContactRequest request) {
        theContactService.update(contactId, request);
        return ResponseEntity.ok(theConversionService
                .convert(theContactService.updateImg(contactId, file), ContactDTO.class));
    }

    @Override
    @DeleteMapping(path = RestConstants.Contact.CONTACT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Delete contact.")
    @PreAuthorize("hasAuthority('contact:write')")
    public void delete(@PathVariable String contactId) {
        theContactService.deleteById(contactId);
    }

    @Override
    @GetMapping(path = RestConstants.Contact.CONTACTS_IMAGE_FILE)
    @ApiOperation("Download contact image.")
    public ResponseEntity<ByteArrayResource> downloadImg(@PathVariable String fileName) {
        ByteArrayResource resource = theContactService.downloadImg(fileName);
        return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .header("Content-Type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @Override
    @PutMapping(path = RestConstants.Contact.CONTACTS_IMAGES_CONTACT, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Update contact image.")
    @PreAuthorize("hasAuthority('contact:write')")
    public ResponseEntity<ContactDTO> updateImg(@PathVariable String contactId,
                                                @RequestBody MultipartFile file) {
        return ResponseEntity.ok(theConversionService.convert(
                theContactService.updateImg(contactId, file), ContactDTO.class
        ));
    }

    @Override
    @DeleteMapping(path = RestConstants.Contact.CONTACT_IMAGE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Delete contact image.")
    @PreAuthorize("hasAuthority('contact:write')")
    public ResponseEntity<ContactDTO> deleteImg(@PathVariable String contactId,
                                                @PathVariable String fileName) {
        return ResponseEntity.ok(theConversionService.convert(
                theContactService.deleteImg(contactId, fileName), ContactDTO.class
        ));
    }

}
