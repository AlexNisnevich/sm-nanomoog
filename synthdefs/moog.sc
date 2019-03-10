// Adapted from https://superdupercollider.blogspot.com/2009/02/this-is-blog-about-supercollider.html
// For basic architecture see http://w2.mat.ucsb.edu/240/A/2017/04/26/supercollider.html
(
SynthDef("Moog",{
    arg osc1Shape = 0, osc2Shape = 2, osc1Level = 0.5, osc2Level = 0.5, volume = 0.5,
        filterCutoff = 2000, filterResonance = 0, filterEnvAmt = 0,
        ampAttack = 0.1, ampDecay = 0.1, ampSustain = 0.7, ampRelease = 0.2,
        filterAttack = 0.1, filterDecay = 0.1, filterSustain = 0.9, filterRelease = 0.2,
        gate = 1, freq = 440;

    var oscArray1 = [Saw.ar(freq), SinOsc.ar(freq), Pulse.ar(freq)];  // TODO pulse width control; more oscillator types
    var oscArray2 = [Saw.ar(freq), SinOsc.ar(freq), Pulse.ar(freq)];
    var ampEnv = EnvGen.ar(Env.adsr(ampAttack, ampDecay, ampSustain, ampRelease, volume), gate, doneAction:2);
    var filterEnv = EnvGen.ar(Env.adsr(filterAttack, filterDecay, filterSustain, filterRelease, filterEnvAmt), gate, doneAction:2);

    // TODO oscillator modulation (frequency, PWM)
    var osc1 = Select.ar(osc1Shape, oscArray1);
    var osc2 = Select.ar(osc2Shape, oscArray2);  // TODO detune; hard sync
    // TODO third or sub oscillator

    // TODO noise source
	var mixer = (osc1 * osc1Level) + (osc2 * osc2Level);  // TODO mixer should "overdrive" at high levels

    // TODO filter and amp modulation
    // TODO the filter cutoff (modulated by envelope) formula isn't quite right
    var filter = MoogFF.ar(mixer, filterCutoff * (1 + filterEnv), filterResonance);
    var amp = filter * ampEnv;

    Out.ar(0, amp)
}).store
)
