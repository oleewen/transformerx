package com.transformer.es.index;

import lombok.Data;

import java.util.List;

@Data
public abstract class Indexes {

    public abstract List<String> getIndexes();
}
