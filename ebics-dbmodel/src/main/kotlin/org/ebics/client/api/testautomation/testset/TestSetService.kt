package org.ebics.client.api.testautomation.testset

import org.ebics.client.api.getById
import org.springframework.stereotype.Service

@Service
class TestSetService(private val testSetRepository: TestSetRepository) {
    fun addTestSet(testSet: TestSetUpdateRequest): Long {
        val testSetEntity = TestSetEntity(null, testSet.name, emptyList(), emptyList())
        testSetRepository.save(testSetEntity)
        return testSetEntity.id!!
    }

    fun updateTestSet(testSetId: Long, testSet: TestSetUpdateRequest) {
        val existingTestSet = testSetRepository.getById(testSetId, "testset")
        existingTestSet.name = testSet.name
        testSetRepository.save(existingTestSet)
    }

    fun deleteTestSet(testSetId: Long) {
        testSetRepository.deleteById(testSetId)
    }
}