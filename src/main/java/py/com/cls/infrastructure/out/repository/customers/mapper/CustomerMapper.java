package py.com.cls.infrastructure.out.repository.customers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerResponse;
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;

import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    Customer toResponse(Customers customers);
    default CustomerResponse<Page<Customer>> mapToResponseList(Page<Customers> customers){
        return Optional.of(customers).map(this::toResponseList)
                .map(response -> CustomerResponse.<Page<Customer>>builder().data(response).build())
                .orElseGet(CustomerResponse::new);
    }
    private Page<Customer> toResponseList(Page<Customers> customers){
        return new PageImpl<>(customers.stream().map(this::toResponse)
                .collect(Collectors.toList()), customers.getPageable(), customers.getTotalElements());
    }
}
