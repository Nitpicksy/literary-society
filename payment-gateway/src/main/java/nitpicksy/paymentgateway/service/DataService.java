package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Data;

import java.util.Set;

public interface DataService {

    Set<Data> create(Set<Data> listData);
}
