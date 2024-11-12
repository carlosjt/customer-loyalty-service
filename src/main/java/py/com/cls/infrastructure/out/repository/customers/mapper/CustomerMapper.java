package py.com.cls.infrastructure.out.repository.customers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerResponse;
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper()
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    Customer toDomain(Customers customers);
    Customers toEntity(Customer customer);
    void mapDomainToEntity(Customer customer, @MappingTarget Customers customers);
    default CustomerResponse<Page<Customer>> mapToDomainList(Page<Customers> customers){
        return Optional.of(customers).map(this::toDomainList)
                .map(response -> CustomerResponse.<Page<Customer>>builder().data(response).build())
                .orElseGet(CustomerResponse::new);
    }
    private Page<Customer> toDomainList(Page<Customers> customers){
        return new PageImpl<>(customers.stream().map(this::toDomain)
                .collect(Collectors.toList()), customers.getPageable(), customers.getTotalElements());
    }
    default List<Customer> toDomainListWithoutPage(List<Customers> customers) {
        return customers.stream().map(this::toDomain).collect(Collectors.toList());
    }
}
