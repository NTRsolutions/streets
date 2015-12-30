package net.blaklizt.streets.api;

/******************************************************************************
 * *
 * Created:     27 / 12 / 2015                                             *
 * Platform:    Red Hat Linux 9                                            *
 * Author:      Tich de Blak (Tsungai Kaviya)                              *
 * Copyright:   Blaklizt Entertainment                                     *
 * Website:     http://www.blaklizt.net                                    *
 * Contact:     blaklizt@gmail.com                                         *
 * *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License as published by    *
 * the Free Software Foundation; either version 2 of the License, or       *
 * (at your option) any later version.                                     *
 * *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.    See the         *
 * GNU General Public License for more details.                            *
 * *
 ******************************************************************************/

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.blaklizt.streets.api.contract.UserLocationHistory;
import net.blaklizt.streets.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@Api(value = "Users", description = "Streets User API")
@RestController
@RequestMapping(value = "/api/users", consumes = "application/json", produces = "application/json")
public class LogResource {

    @Autowired
    UserService userService;

    @ApiOperation(value = "Post user's current location")
    @RequestMapping(value="/{user_id}/location", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserLocationHistory postUserLocation(@PathVariable("user_id") Long userId,
                                 UserLocationHistory userLocationHistory, HttpServletResponse response)
    {
        UserLocationHistory postResponse = userService.postUserLocation(userLocationHistory);
        String locationURL = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("{id}").buildAndExpand(userLocationHistory.getId())
                .toUriString();

        response.setHeader("Location", locationURL);
        return postResponse;
    }

    @ApiOperation(value = "Test")
    @RequestMapping(value="/", method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getApplicationName()
    {
        return "I'm the streets, look both ways before you cross me!";
    }
}