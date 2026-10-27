package org.example;

//El cliente solo conoce esta interface, las otras no(?
public interface OutfitFactory {
    Jean crearJean();
    Remera crearRemera();
}
