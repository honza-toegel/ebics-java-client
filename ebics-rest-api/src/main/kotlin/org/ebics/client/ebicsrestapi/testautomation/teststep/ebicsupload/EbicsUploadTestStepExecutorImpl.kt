package org.ebics.client.ebicsrestapi.testautomation.teststep.ebicsupload

import org.ebics.client.api.testautomation.testexecution.TestExecutionContext
import org.ebics.client.api.testautomation.teststep.TestStepExecutor
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepDataInput
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepDataOutput
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepDefinition
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepExecutor
import org.ebics.client.api.trace.BankConnectionTraceSession
import org.ebics.client.api.trace.IFileService
import org.ebics.client.api.trace.orderType.OrderTypeDefinition
import org.ebics.client.ebicsrestapi.bankconnection.UserIdPass
import org.ebics.client.ebicsrestapi.bankconnection.session.IEbicsSessionFactory
import org.ebics.client.filetransfer.h004.FileUpload
import org.ebics.client.order.EbicsAdminOrderType
import org.ebics.client.order.h004.EbicsUploadOrder
import org.springframework.stereotype.Service

@Service
class EbicsUploadTestStepExecutorImpl(
    private val sessionFactory: IEbicsSessionFactory,
    private val fileService: IFileService,
    private val fileUploadService: FileUpload,
) : TestStepExecutor<EbicsUploadTestStepDefinition, EbicsUploadTestStepDataInput, EbicsUploadTestStepDataOutput>,
    EbicsUploadTestStepExecutor {
    override fun executeStep(
        testStepDefinition: EbicsUploadTestStepDefinition,
        testStepDataInput: EbicsUploadTestStepDataInput,
        testExecutionContext: TestExecutionContext
    ): EbicsUploadTestStepDataOutput {
        val session = sessionFactory.getSession(UserIdPass(testExecutionContext.bankConnectionId, testExecutionContext.bankConnectionPassword))
        val order =
            EbicsUploadOrder(testStepDefinition.orderType004.orderType, testStepDefinition.orderType004.attributeType, testStepDefinition.orderType004.params)
        val orderType = OrderTypeDefinition(EbicsAdminOrderType.UPL, businessOrderType = testStepDefinition.orderType004.orderType)
        val traceSession = BankConnectionTraceSession(session, orderType)
        val response = fileUploadService.sendFile(session, traceSession, testStepDataInput.inputFileContent, order)
        fileService.addUploadedFile(traceSession, testStepDataInput.inputFileContent)
        return EbicsUploadTestStepDataOutput(response.orderNumber, response.transactionId)
    }
}