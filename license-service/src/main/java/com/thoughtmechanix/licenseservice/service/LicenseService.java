package com.thoughtmechanix.licenseservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenseservice.config.ServiceConfig;
import com.thoughtmechanix.licenseservice.model.License;
import com.thoughtmechanix.licenseservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {
	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;

	public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		return license;
	}

	//@HystrixCommand(commandProperties = {@HystrixProperty (name = "execution.isolation.thread.timeoutInMilliseconds",value = "12000")})
	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList")
	public List<License> findByOrganizationId(String organizationId) {
		List<License> licenses = licenseRepository.findByOrganizationId(organizationId);
		return licenses;
	}

	public void saveLicense(License license) {
		license.withId(UUID.randomUUID().toString());
		licenseRepository.save(license);
	}

	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		return license;
	}

	private List<License> buildFallbackLicenseList() {
		List<License> fallbackList = new ArrayList<>();
		License license = new License().withId("2222");
		fallbackList.add(license);
		return fallbackList;
	}
}
