package com.connfa.schedule.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Schedule {
    public static Schedule create(List<SchedulePage> pages) {
        return new AutoValue_Schedule(pages);
    }

    public abstract List<SchedulePage> pages();
}
