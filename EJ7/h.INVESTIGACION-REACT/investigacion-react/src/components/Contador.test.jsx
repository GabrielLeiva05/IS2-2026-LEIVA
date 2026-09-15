import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Contador from "./Contador";

test("el contador comienza en 0", () => {
    render(<Contador />);
    expect(screen.getByText("Valor: 0")).toBeInTheDocument();
});

test("el contador aumenta al presionar el botón", async () => {
    const user = userEvent.setup();
    render(<Contador />);

    const boton = screen.getByRole("button", {
        name: "Incrementar"
    });

    await user.click(boton);

    expect(screen.getByText("Valor: 1")).toBeInTheDocument();
});