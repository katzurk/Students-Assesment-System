import {ToggleButton, ToggleButtonGroup} from "@mui/material";
import { styled } from '@mui/material/styles';
import {useState} from "react";

const FilterToggleButton = styled(ToggleButton)(() => ({
    color: 'var(--foreground)',
    borderColor: 'var(--foreground)',
    borderRadius: 'var(--border-radius)',
    '&:hover': {
        backgroundColor: 'var(--button-primary-hover)',
        borderColor: 'var(--foreground)',
    },
    '&.Mui-selected': {
        backgroundColor: 'var(--foreground)',
        color: 'var(--background)',
        '&:hover': {
            backgroundColor: 'var(--button-primary-hover)',
        },
    },
}));

interface IFilterButtons {
    filter: string,
    setFilter: React.Dispatch<React.SetStateAction<string>>;
}

export const FilterButtons = ({filter, setFilter}: IFilterButtons) => {
    const handleFilter = (event: React.MouseEvent<HTMLElement>, newFilter: string) => {
        setFilter(newFilter);
    }

    return (
        <ToggleButtonGroup
            value={filter}
            exclusive
            onChange={handleFilter}
            sx={{ color: 'white' }}
        >
            <FilterToggleButton value="all">All</FilterToggleButton>
            <FilterToggleButton value="registered">Registered</FilterToggleButton>
            <FilterToggleButton value="not registered">Not Registered</FilterToggleButton>
        </ToggleButtonGroup>
    )
}