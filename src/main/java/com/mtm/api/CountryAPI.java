package com.mtm.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
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
        data.getResponseCountry().clear();
        for (i = 0; i < countrySize && count < 5; i++) {
            if (((word.length() + 2) == data.getAllCountry().get(i).getName().length()
                    || (word.length() + 1) == data.getAllCountry().get(i).getName().length())
                    && data.getAllCountry().get(i).getCountry().equals("TR")
                    && findWord(word, data.getAllCountry().get(i).getName())) {
                data.getResponseCountry().add(data.getAllCountry().get(i));
                flag = 1;
                count++;
            }

        }
        for (i = 0; i < countrySize; i++) {
            if (word.length() >= data.getAllCountry().get(i).getName().length() && findWord(word, data.getAllCountry().get(i).getName())) {
                data.getResponseCountry().add(data.getAllCountry().get(i));
                flag = 1;
                count++;
            }

        }
        System.out.println("count: " + count);
        if (count < 5) {
            for (i = 0; i < countrySize && data.getResponseCountry().size() < 5; i++) {
                if (findWord(word, data.getAllCountry().get(i).getName())) {
                    data.getResponseCountry().add(data.getAllCountry().get(i));
                    Set<Country> set = new HashSet<>(data.getResponseCountry());
                    data.getResponseCountry().clear();
                    data.getResponseCountry().addAll((Collection<? extends Country>) set);

                }
            }
            System.out.println("countson: " + count);

            this.setTurkey();
            return new ResponseEntity<>(data.getResponseCountry(), HttpStatus.OK);
        } else if (flag == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {

            Set<Country> set = new HashSet<>(data.getResponseCountry());
            data.getResponseCountry().clear();
            data.getResponseCountry().addAll((Collection<? extends Country>) set);
            this.setTurkey();
            return new ResponseEntity<>(data.getResponseCountry(), HttpStatus.OK);
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

    private void setTurkey() {
        int count = 0;
        for (int i = 0; i < data.responseCountry.size(); i++) {
            if (data.responseCountry.get(i).getCountry().equals("TR")) {
                Country tmp = data.getResponseCountry().get(i);
                data.getResponseCountry().remove(i);
                data.getResponseCountry().add(0, tmp);

            }
        }
    }

}
