package space.teymurov.parsingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class AbstractCodeBattles {
    protected final String status;
    protected final String message;
}
