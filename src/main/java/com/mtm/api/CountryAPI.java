package com.mtm.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class CountryAPI {

    Data data;

    public CountryAPI() throws FileNotFoundException, IOException, ParseException {
        this.data = new Data();
        data.readJsonFile("berkan");
    }

    @RequestMapping(value = "/{word}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<ArrayList<Country>> requestWord(@PathVariable("word") String word) {
        int flag = 0;
        int i = 0, countrySize = data.getAllCountry().size();
        int count = 0;
        Data localData = new Data();

        for (i = 0; i < countrySize && count < 5; i++) {
            if (((word.length() + 2) == data.getAllCountry().get(i).getName().length()
                    || (word.length() + 1) == data.getAllCountry().get(i).getName().length())
                    && data.getAllCountry().get(i).getCountry().equals("TR")
                    && findWord(word, data.getAllCountry().get(i).getName())) {
                localData.getResponseCountry().add(data.getAllCountry().get(i));
                flag = 1;
                count++;
            }

        }
        for (i = 0; i < countrySize; i++) {
            if (word.length() >= data.getAllCountry().get(i).getName().length() && findWord(word, data.getAllCountry().get(i).getName())) {
                localData.getResponseCountry().add(data.getAllCountry().get(i));
                flag = 1;
                count++;
            }

        }

        if (count < 5) {
            for (i = 0; i < countrySize && localData.getResponseCountry().size() < 5; i++) {
                if (findWord(word, data.getAllCountry().get(i).getName())) {
                    localData.getResponseCountry().add(data.getAllCountry().get(i));
                    Set<Country> set = new HashSet<>(localData.getResponseCountry());
                    localData.getResponseCountry().clear();
                    localData.getResponseCountry().addAll((Collection<? extends Country>) set);

                }
            }

            localData.setTurkey();
            log.info("Returning response with word:{}, response: {}", word, localData.getResponseCountry().toString());
            return new ResponseEntity<>(localData.getResponseCountry(), HttpStatus.OK);
        } else if (flag == 0) {
            log.error("Bad request is returning because flag is 0, with search word: {}", word);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {

            Set<Country> set = new HashSet<>(localData.getResponseCountry());
            localData.getResponseCountry().clear();
            localData.getResponseCountry().addAll((Collection<? extends Country>) set);
            localData.setTurkey();
            log.info("Returning response with word:{}, response: {}", word, localData.getResponseCountry().toString());
            return new ResponseEntity<>(localData.getResponseCountry(), HttpStatus.OK);
        }
    }

    private boolean findWord(String word, String city) {
        for (int i = 0; i < word.length();) {
            if (i < city.length() && Character.toLowerCase(word.charAt(i)) == Character.toLowerCase(city.charAt(i))) {
                i++;
            } else {
                return false;
            }
        }
        return true;
    }
}
