package py.com.cls.infrastructure.out.repository.point_usage_headers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;
import py.com.cls.infrastructure.out.repository.point_concepts.entity.PointsConcept;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_points_usage_header")
public class PointsUsageHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customers customer;

    @Column(name = "total_points_used", nullable = false)
    private Integer totalPointsUsed;

    @Column(name = "usage_date", nullable = false)
    private LocalDateTime usageDate;

    @ManyToOne
    @JoinColumn(name = "usage_concept_id", nullable = false)
    private PointsConcept usageConcept;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

