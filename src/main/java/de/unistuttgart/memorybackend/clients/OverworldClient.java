package de.unistuttgart.memorybackend.clients;

import de.unistuttgart.memorybackend.data.KeybindingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "overworldClient", url = "${overworld.url}/players")
public interface OverworldClient {
    @GetMapping("/{playerId}/keybindings/{binding}")
    KeybindingDTO getKeybindingStatistic(
            @PathVariable("playerId") final String playerId,
            @PathVariable("binding") final String binding,
            @CookieValue("access_token") final String accessToken
    );
}