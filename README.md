<div align="center">

# OPERATING SYSTEMS: THE BOSS-RUSH REPO
### taught by JOSIAAAAH  (ง'̀-'́)ง   |   os-Michelle  (•_•)   |   Aslam  (ಠ_ಠ)

![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=750&center=true&vCenter=true&width=980&lines=If+it+isn’t+scheduled%2C+it+doesn’t+run.;If+it+isn’t+protected%2C+it+gets+clapped.;If+it+isn’t+paged%2C+it+thrashes.;We+are+not+here+to+cope.+We+are+here+to+CONVERGE.)

<img src="https://media.giphy.com/media/l0HlBO7eyXzSZkJri/giphy.gif" width="520" alt="lock in"/>

</div>

---

## What this repo is
This is a combo-type OS dojo:
we gather notes, labs, past-paper tricks, and mental models until the course becomes muscle memory.

> The kernel doesn’t panic.
> We don’t panic.
> We debug.

---

## The Three Lecturers: The Council of Context Switches

### JOSIAAAAH (The Network Baddie)  (ง'̀-'́)ง
- If it can run, it *must* be scheduled.
- If it waits, it must have a reason.
- If it starves, we fix it.

<img src="https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif" width="450" alt="speed"/>

### Aslam (The Memory Whisperer)  (•_•)
- Pages. Frames. Tables. Translation Lookaside Buffer.
- If you don’t translate addresses correctly, you are not invited to physical memory.

<img src="https://media.giphy.com/media/26BRv0ThflsHCqDrG/giphy.gif" width="450" alt="training"/>

### Michelle (The Concurrency Exorcist)  (ಠ_ಠ)
- If it races, we lock.
- If it deadlocks, we diagnose.
- If it “works on my machine”, we still verify.


> If GIFs don’t load on your WiFi, imagine the same energy but with less bandwidth.
> (Still valid.)

---

## The OS Arc List (what we are marinating)
> The goal is not to recognise terms.
> The goal is to predict behaviour.

### Processes & Threads
- Process state model: ready / running / waiting
- Context switch: who saves what, where, and why it hurts

### CPU Scheduling
- First-Come-First-Served / Shortest Job First / Shortest Remaining Time First / Round Robin / Priority
- Starvation & ageing
- Gantt charts without tears

### Concurrency & Synchronisation
- critical sections
- locks / monitors / condition variables
- deadlock conditions, prevention/avoidance/detection

### Memory Management
- paging, frames, page tables, Translation Lookaside Buffer
- logical vs physical addresses
- page replacement (First-In First-Out / Least Recently Used / Optimal) & why First-In First-Out can embarrass you

### Storage / I/O / File Systems (if in your syllabus)
- buffers, caches, and why your disk is not your friend

---


*Solving Mutexes under immense pressure, only the exam is left now - e1 - pred48
