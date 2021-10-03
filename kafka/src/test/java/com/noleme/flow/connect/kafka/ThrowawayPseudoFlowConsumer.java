package com.noleme.flow.connect.kafka;

import com.noleme.flow.Flow;
import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.generator.ProducerGenerator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.noleme.flow.interruption.InterruptionException.interrupt;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public class ThrowawayPseudoFlowConsumer
{
    public static void main(String[] args) throws CompilationException, RunException
    {
        Instant end = Instant.now().plus(2, ChronoUnit.MINUTES);
        Queue<String> messages = new ConcurrentLinkedQueue<>();

        var setup = Flow
            .stream(() -> new ProducerGenerator<>(Instant::now, instant -> instant == null || instant.isBefore(end))).setMaxParallelism(4)
            .sink(instant -> messages.add(instant.toString()))
        ;

        var flow = Flow
            .stream(() -> new ConsumerGenerator<>(messages)).setMaxParallelism(8)
            .pipe(msg -> "received:" + msg)
            .sink(System.out::println)
        ;

        Flow.runAsParallel(12, setup, flow);
    }

    public static class ConsumerGenerator<T> implements Generator<T>
    {
        private final Queue<T> bus;
        private boolean isActive;

        public ConsumerGenerator(Queue<T> bus)
        {
            this.bus = bus;
            this.isActive = true;
        }

        @Override
        public boolean hasNext()
        {
            return this.isActive;
        }

        @Override
        public T generate() throws GenerationException
        {
            try {
                while (this.isActive && this.bus.isEmpty())
                    Thread.sleep(100);

                if (!this.isActive)
                    throw interrupt();

                return this.bus.poll();
            }
            catch (InterruptedException e) {
                throw new GenerationException(e.getMessage(), e);
            }
        }

        public void deactivate()
        {
            this.isActive = false;
        }
    }
}
