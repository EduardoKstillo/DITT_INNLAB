import { Component, Input, forwardRef, Output, EventEmitter } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-device-counter',
  templateUrl: './device-counter.component.html',
  styleUrls: ['./device-counter.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DeviceCounterComponent),
      multi: true
    }
  ]
})
export class DeviceCounterComponent {
  @Input() min: number = 0;    // Mínimo siempre será 0
  @Input() max: number = 0;    // Máximo basado en la cantidad del dispositivo
  @Input() initialValue: number = 0;
  @Output() valueChange: EventEmitter<number> = new EventEmitter<number>();

  value: number = this.initialValue;

  ngOnInit() {
    if (this.initialValue < this.min) {
      this.value = this.min;
    } else if (this.initialValue > this.max) {
      this.value = this.max;
    } else {
      this.value = this.initialValue;
    }
  }

  onChange = (counterValue: number) => {};
  onTouched = () => {};

  constructor() {
    this.value = this.initialValue;
  }

  writeValue(counterValue: number): void {
    this.value = counterValue;
  }

  registerOnChange(fn: (counterValue: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  increment() {
    if (this.value < this.max) {
      this.value++;
      this.valueChange.emit(this.value); // Notifica a Angular el cambio
    }
  }

  decrement() {
    if (this.value > this.min) {
      this.value--;
      this.valueChange.emit(this.value); // Notifica a Angular el cambio
    }
  }

  isAtMin() {
    return this.value === this.min;
  }

  isAtMax() {
    return this.value === this.max;
  }
}
