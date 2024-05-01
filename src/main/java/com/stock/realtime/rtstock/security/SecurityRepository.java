package com.stock.realtime.rtstock.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityRepository extends CrudRepository<Security, String> {

    List<Security> findAllByType(SecurityType type);

    List<Security> findAllByTypeNot(SecurityType type);

    List<Security> findAllByUnderlyingStock(String underlyingStock);
}
