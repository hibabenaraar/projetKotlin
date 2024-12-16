package com.example.projinteg.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
public class ServiceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="service_name")
	private String serviceName;

	@OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Worker> workers = new HashSet<>();

	public ServiceEntity() {
	}

	public ServiceEntity(Long id, String serviceName) {
		this.id = id;
		this.serviceName = serviceName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Set<Worker> getWorkers() {
		return workers;
	}
	public void setWorkers(Set<Worker> workers) {
		this.workers = workers;
	}
}
