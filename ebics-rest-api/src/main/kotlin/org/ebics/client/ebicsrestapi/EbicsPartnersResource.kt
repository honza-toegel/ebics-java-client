package org.ebics.client.ebicsrestapi

import org.ebics.client.api.partner.Partner
import org.ebics.client.api.partner.PartnerService
import org.ebics.client.api.user.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("partners")
@CrossOrigin(origins = ["http://localhost:8080"])
class EbicsPartnersResource (
    private val partnerService: PartnerService)
{
    @GetMapping("")
    fun listPartners(): List<Partner> = partnerService.findAll()

    @GetMapping("{id}")
    fun getPartnerById(@PathVariable partnerId: Long): Partner = partnerService.getPartnerById(partnerId)

    @DeleteMapping("{id}")
    fun deletePartnerById(@PathVariable partnerId: Long) = partnerService.deletePartnerById(partnerId)

    @PostMapping("")
    fun createPartner(@RequestParam ebicsPartnerId: String, @RequestParam bankId: Long): Partner =
        partnerService.createOrGetPartner(ebicsPartnerId, bankId)
}