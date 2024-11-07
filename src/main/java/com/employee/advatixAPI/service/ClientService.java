package com.employee.advatixAPI.service;

import com.employee.advatixAPI.dto.ClientResponse;
import com.employee.advatixAPI.entity.address.City;
import com.employee.advatixAPI.entity.carrier.ClientCarrierInfo;
import com.employee.advatixAPI.entity.client.ClientInfo;
import com.employee.advatixAPI.entity.address.Country;
import com.employee.advatixAPI.entity.address.States;
import com.employee.advatixAPI.exception.NotFoundException;
import com.employee.advatixAPI.repository.client.CityRepository;
import com.employee.advatixAPI.repository.client.ClientRepository;
import com.employee.advatixAPI.repository.client.CountryRepository;
import com.employee.advatixAPI.repository.client.StateRepository;
import com.employee.advatixAPI.repository.partner.ClientCarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ClientCarrier clientCarrierRepository;

    public ClientResponse getClientById(Integer clientId) {
        ClientResponse clientResponse = new ClientResponse();
        Optional<ClientInfo> clientInfo = clientRepository.getClientByClientId(clientId);
        if (clientInfo.isPresent()){
            Country country = countryRepository.getCountryByCountryId(clientInfo.get().getCountryId());
            States state = stateRepository.getStatesByStateIdAndCountryId(clientInfo.get().getStateId(),country.getCountryId());
            City city = cityRepository.getCityByStateIdAndCountryId(state.getStateId(), country.getCountryId());

            clientResponse.setClient(clientInfo.get());
            clientResponse.setCountry(country);
            clientResponse.setStates(state);
            clientResponse.setCity(city);

            return clientResponse;
        }
        return null;
    }

    public void createClient(ClientInfo clientInfo) {

        if(countryRepository.getCountryByCountryId(clientInfo.getCountryId()) == null){
            throw new NotFoundException("Country with id " + clientInfo.getCountryId() + " not found");
        }
        if(stateRepository.getStatesByStateId(clientInfo.getStateId()) == null){
            throw new NotFoundException("State with id " + clientInfo.getStateId() + " not found");
        }
        if (cityRepository.getCityByCityId(clientInfo.getCityId()) == null) {
            throw new NotFoundException("City with id " + clientInfo.getCityId() + " not found");
        }

        ClientCarrierInfo clientPartner = new ClientCarrierInfo();
        if(clientInfo.getPartnerId() != null){
            clientPartner.setClientId(clientInfo.getClientId());
            clientPartner.setPartnerId(clientInfo.getPartnerId());
            clientCarrierRepository.save(clientPartner);
        }
        clientRepository.save(clientInfo);

        System.out.println(clientInfo);
    }

    public String editClientDetails(ClientInfo clientDetails) {

        Optional<ClientInfo> client = clientRepository.getClientByClientId(clientDetails.getClientId());
        if(client.isPresent()){
            client.get().setClientEmail(clientDetails.getClientEmail());
            client.get().setClientName(clientDetails.getClientName());
            client.get().setClientPhone(clientDetails.getClientPhone());
            client.get().setAddress(clientDetails.getAddress());
            client.get().setCityId(clientDetails.getCityId());
            client.get().setStateId(clientDetails.getStateId());
            client.get().setCountryId(clientDetails.getCountryId());
        }

        clientRepository.save(client.get());
        System.out.println(client);

        return "Success";
    }


}
